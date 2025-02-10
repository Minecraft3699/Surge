{ pkgs ? import <nixpkgs> {} }:

(pkgs.buildFHSEnv {
  name = "mod-dev-env";
  runScript = "bash -c 'nohup idea-community >/dev/null 2>&1 & exec bash'";
}).env
