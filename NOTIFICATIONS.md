# Email and SMS integration

The consolidated service listens on its existing port 8082. It forwards requests
to EmailSmsService at `http://localhost:9088` by default; override
`NOTIFICATION_SERVICE_URL` with the reachable service URL. For separate Docker
containers, `localhost` refers to the consolidated container: configure the
notification service hostname and port on a shared network instead. Keep the
upstream service private, since it does not authenticate these requests.

All routes require a Keycloak token with `ROLE_CLIENT` plus the indicated role.
Create and assign the roles in Keycloak before use.

| Consolidated POST route | Additional role | Downstream POST route |
| --- | --- | --- |
| `/api/service/notifications/email` | `ROLE_SEND_EMAIL` | `/sendMail` |
| `/api/service/notifications/sms` | `ROLE_SEND_SMS` | `/api/vi/sendsms` |
| `/api/service/notifications/sms/bulk` | `ROLE_SEND_SMS` | `/api/vi/sendbulksms` |

Requests use the existing EmailSmsService JSON contract, including its `refrence`
field for single SMS. Email takes `recipient`, `subject`, and `msgBody`; SMS takes
`from`, `to`, `content`, `refrence`, and `callbackUrl`. Bulk SMS takes `from`,
`content`, `callbackUrl`, and a `recipients` array containing `to` and `reference`.
The downstream response status, content type, and body are passed through.

EmailSmsService is configured to listen on port 9088. Ensure that port is free
on its host before starting it. Its SMTP and SMS credentials must be configured
there; they are not needed by the consolidated service.
