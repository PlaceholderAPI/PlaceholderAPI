name: Bug Report
description: Found a Bug about PlaceholderAPI? Use this template to report it!
labels:
- "Type: Issue (Unconfirmed)"
body:
- type: markdown
  attributes:
    value: |-
      Thank you for taking your time and opening a Bug Report.
      In order for us to process this Bug Report as fast and efficient as possible do we ask you to read the form carefully and provide any requested information.
      Required fields are marked with an asterisk symbol (`*`)
      
      Also, always make sure to use the latest Release from [Spigot](https://www.spigotmc.org/resources/6245/) or the latest Development Build from our [Jenkins Server](http://ci.extendedclip.com/job/PlaceholderAPI/) to make sure that your issue isn't already fixed.
      
      **DO NOT REPORT ISSUES WITH EXPANSIONS AND/OR PLACEHOLDERS. USE THE APPROPRIATE ISSUE TRACKER FOR THOSE!**
- type: checkboxes
  attributes:
    label: Confirmation
    description: Please make sure to have followed the following checks.
    options:
      - label: My issue isn't already found on the Issue tracker.
        required: true
      - label: My issue is about **PlaceholderAPI** and not any expansion or external plugin
        required: true
      - label: The issue isn't already fixed in a Spigot Release or Development Build.
        required: true
- type: dropdown
  attributes:
    label: "Type"
    description: |-
      What kind of Bug do you encounter?
      
      - `Plugin Bug`: PlaceholderAPI doesn't startup properly.
      - `API Bug`: A method you use didn't work or has an unexpected result.
      - `Plugin/Server Incompatability`: PlaceholderAPI either doesn't support a specific Server Type/Version or has conflicts with another plugin.
    multiple: false
    options:
      - "Plugin Bug"
      - "API Bug"
      - "Plugin/Server Incompatability"
  validations:
    required: true
- type: textarea
  attributes:
    label: "What happens?"
    description: "What bug are you encountering? Try to explain it as detailed as possible."
    placeholder: "PlaceholderAPI does this when I do that..."
  validations:
    required: true
- type: textarea
  attributes:
    label: "Expected Behaviour"
    description: "What behaviour did you expect from PlaceholderAPI?"
    placeholder: "PlaceholderAPI should actually do..."
  validations:
    required: true
- type: textarea
  attributes:
    label: "How to Reproduce"
    description: |-
      List the steps on how to reproduce this Bug.
      Post any Code-examples in the `Additional Information` field below when you selected `API Bug`.
    placeholder: |-
      1. Do this
      2. ...
      3. Profit!
  validations:
    required: true
- type: input
  id: "dump"
  attributes:
    label: "`/papi dump` Output"
    description: |-
      Please execute the `/papi dump` command and provide the generated URL from it.
      If you can't execute the command (i.e. PlaceholderAPI doesn't start up) can you put N/A here and mention the issue in the `Additional Information` field.
    placeholder: "https://paste.helpch.at/dump.log"
  validations:
    required: true
- type: input
  id: "console"
  attributes:
    label: "Console Log"
    description: |-
      Get the latest content of your `latest.log` file an upload it to https://paste.helpch.at
      Take the generated URL and paste it into this field.
    placeholder: "https://paste.helpch.at/latest.log"
- type: input
  id: "error"
  attributes:
    label: "Errors"
    description: |-
      Upload any errors you find to https://paste.helpch.at and post the link in the field below.
    placeholder: "https://paste.helpch.at/error.log"
- type: textarea
  attributes:
    label: "Additional Info"
    description: |-
      Add any extra info you think is nessesary for this Bug report.
      - If you selected `API Bug` will you need to include code-examples here to reproduce the issue.
      - If you selected `Plugin/Server Incompatability` should you include extra Server info such as a Timings or Spark-Report or info about the plugin in question.
    placeholder: "Put any extra info you like into this field..."
