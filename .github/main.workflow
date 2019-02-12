workflow "fast-prototype" {
  on = "pull_request"
  resolves = ["branch cleanup", "auto-pull-request"]
}

action "branch cleanup" {
  uses = "jessfraz/branch-cleanup-action@master"
  secrets = ["GITHUB_TOKEN"]
}

action "auto-pull-request" {
  uses = "funivan/github-autopr@0.1.0"
  secrets = ["GITHUB_TOKEN"]
  args = ".head_commit.message .*#pr.*"
}
