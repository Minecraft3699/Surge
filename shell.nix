{ pkgs ? import <nixpkgs> {} }:

(pkgs.buildFHSEnv {
  name = "mod-dev-env";
  targetPkgs = pkgs: with pkgs; [
    # graphics dependencies
    libGL
    libglvnd
    vulkan-loader
    xorg.libX11
    xorg.libXcursor
    xorg.libXrandr
    xorg.libXxf86vm

    # audio dependencies
    openal alsa-lib libpulseaudio
  ];
  profile = ''
    export ALSA_PLUGIN_DIR=${pkgs.alsa-plugins}/lib/alsa-lib
    export LD_LIBRARY_PATH=${pkgs.lib.makeLibraryPath [ pkgs.alsa-lib pkgs.libpulseaudio ]}:$LD_LIBRARY_PATH
  '';
  runScript = "bash -c 'nohup idea-community >/dev/null 2>&1 & exec bash'";
  multiPkgs = pkgs: with pkgs; [ zlib ];
}).env
