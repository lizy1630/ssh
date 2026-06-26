# Email templates

Drop one Markdown file per template here. Reference a template by name in a request,
e.g. *"write Sarah an email using rental 1st inquiry"* — the agent fuzzy-matches the
name to a file (`rental 1st inquiry` → `rental_first_inquiry.md`), fills it in, and
creates a draft.

## Format

Optional YAML front-matter, then the body:

```markdown
---
account: outlook        # outlook | gmail  (which account to use)
subject: "Rental Inquiry — {{property}}"
---
Hi {{name}},

... body with {{placeholders}} ...
```

- **`account`** — `outlook` for client/work mail, `gmail` for personal. Omit to let the
  agent decide.
- **`subject`** — supports `{{placeholders}}` too.
- **`{{placeholders}}`** — the agent fills these from your request. Anything it can't
  determine is left as a visible `[FILL: ...]` marker so you can complete it before sending.

## Adding a template
Just add a new `.md` file. The filename (words, any separators) is what you say to invoke
it. Examples:
- `rental_first_inquiry.md` → "rental first inquiry", "rental 1st inquiry"
- `viewing_confirmation.md` → "viewing confirmation"
- `late_rent_reminder.md` → "late rent reminder"

Drafts are created for review by default — the assistant won't send unless you say "send it".
