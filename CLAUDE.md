# CLAUDE.md

This is a Jekyll 4 personal website deployed via GitHub Actions.

## Branch Strategy
- `source` - Contains Jekyll source files (edit this branch)
- `main` - Contains built static site (auto-generated, do not edit)

## Development
```bash
bundle install            # Install dependencies
bundle exec jekyll serve  # Local server at http://127.0.0.1:4000
```

## Deployment
Push to `source` branch triggers GitHub Actions workflow that builds and deploys to `main`.

## Key Features
- **jekyll-scholar** - Academic bibliography from `_bibliography/lorcancoyle.bib`
- **jekyll-paginate** - Blog post pagination
- Custom `feed.xml` for RSS
