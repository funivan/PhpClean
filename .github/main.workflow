workflow "fast-prototype" {
  on = "pull_request"
  resolves = ["branch cleanup", "auto-pull-request"]
}

action "branch cleanup" {
  uses = "jessfraz/branch-cleanup-action@master"
  secrets = ["GITHUB_TOKEN"]
}

action "auto-pull-request" {
  uses = "repetitive/actions/auto-pull-request@master"
  secrets = ["GITHUB_TOKEN"]
}
