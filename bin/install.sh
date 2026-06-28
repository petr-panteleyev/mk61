#!/bin/sh

if [ -z "$1" ]
then
  echo "Usage: install.sh <install dir>"
  exit
fi

LAUNCH_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
INSTALL_DIR=$1/mk61

echo -n "Installing into $INSTALL_DIR... "
mkdir -p $INSTALL_DIR
rm -rf $INSTALL_DIR/*
cp -r $LAUNCH_DIR/../target/dist/MK-61/* $INSTALL_DIR
echo "done"

echo -n "Creating desktop entry... "
echo "[Desktop Entry]
Type=Application
Version=1.5
Name=MK-61
Name[ru_RU]=МК-61
Comment=MK-61 emulator
Comment[ru_RU]=Эмулятор микрокалькулятора МК-61
Icon=$INSTALL_DIR/lib/MK-61.png
Exec=$INSTALL_DIR/bin/MK-61
Categories=Game;Java;
" > $HOME/.local/share/applications/mk61.desktop
echo "done"
