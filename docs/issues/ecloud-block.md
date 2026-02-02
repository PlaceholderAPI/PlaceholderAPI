---
description: Fixing blocked connections to the PlaceholderAPI Expansion Cloud (eCloud).
---

# eCloud connection blocked

If you cannot download expansions via PlaceholderAPI, your server may be unable to reach the PlaceholderAPI Expansion Cloud (eCloud). This is usually caused by a network restriction by your host, firewall, or ISP.

## Symptoms

Common signs include:

- You were directed to this page via an in-game message.
- `/papi ecloud download/update <expansion>` fails (timeouts, errors, connection refused, or similar).

## Why it happens

The download command requires outbound HTTPS access from your server to PlaceholderAPI’s eCloud API. Connections can be blocked by:

- Your server host’s firewall / security policy
- A local firewall on the machine
- A network firewall (datacenter, router)
- DNS filtering or an ISP block

## Domains PlaceholderAPI uses

Allow outbound HTTPS (TCP 443) to these domains:

- PlaceholderAPI **< 2.12.0** uses `api.extendedclip.com`
- PlaceholderAPI **>= 2.12.0** uses `*.placeholderapi.com`

## Fix options

### 1) Ask your host to unblock the eCloud

If you use shared hosting or a managed host, you may not be able to change firewall rules yourself. Contact your host and ask them to allow outbound HTTPS requests to the domains listed above.

### 2) Check firewalls / filters you control

If you manage your own server, check any outbound filtering:

- OS firewall rules (Windows Firewall / `ufw` / `iptables`)
- Router/datacenter firewall rules
- DNS filtering

### 3) Manually install expansions (works even if the eCloud is blocked)

If unblocking is not an option, you can download expansions manually:

1. Go to https://ecloud.placeholderapi.com/
2. Open the expansion you want.
3. Download the `.jar`.
4. Put it into your server’s `plugins/PlaceholderAPI/expansions/` folder.
5. Run [`/papi reload`](../users/commands.md#papi-reload) (or restart the server).

## Quick connectivity checks (optional)

If you have access to the machine running the server, these checks from a terminal or command prompt can help confirm a block.

```bash
curl -I https://ecloud.placeholderapi.com/api/v3/
curl -I https://api.extendedclip.com/v2/
```

If these commands fail on the server but work on your own PC/network, the server host or datacenter network is most likely blocking outbound connections.

## Still stuck?

If you need help, join the [Discord Server](https://discord.gg/helpchat) and share:

- What you've tried
- The full console error from the download attempt
- The link provided by `/papi dump`

