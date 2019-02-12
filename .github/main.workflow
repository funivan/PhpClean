workflow "cleanup" {
  on = "pull_request"
  resolves = ["branch cleanup"]
}

action "branch cleanup" {
  uses = "jessfraz/branch-cleanup-action@master"
  secrets = ["GITHUB_TOKEN"]
}

workflow "fast-prototype" {
  on = "push"
  resolves = ["auto-pull-request"]
}

action "auto-pull-request" {
  uses = "funivan/github-autopr@0.1.0"
  secrets = ["GITHUB_TOKEN"]
  args = ".head_commit.message .*#pr.*"
}
