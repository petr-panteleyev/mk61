#!/bin/sh

if [ -z "$1" ]
then
  echo "Usage: sudo install.sh <install dir>"
  exit
fi

LAUNCH_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
INSTALL_DIR=$1/mk61

echo -n "Installing into $INSTALL_DIR... "
mkdir -p $INSTALL_DIR
rm -rf $INSTALL_DIR/*
cp $LAUNCH_DIR/../icons/icon.png $INSTALL_DIR
cp -r $LAUNCH_DIR/../target/jlink/* $INSTALL_DIR

echo "
#!/bin/sh
$INSTALL_DIR/bin/java \\
  -XX:NewRatio=1 \\
  -Xms100m \\
  -Xmx100m \\
  -XX:+AutoCreateSharedArchive \\
  -XX:SharedArchiveFile=\$TMP/mk61.jsa \\
  --enable-native-access=javafx.graphics \\
  --sun-misc-unsafe-memory-access=allow \\
  --module mk/org.panteleyev.mk61.Mk61Application
" > $INSTALL_DIR/mk61.sh

chmod +x $INSTALL_DIR/mk61.sh

echo "[Desktop Entry]
Type=Application
Version=1.5
Name=МК-61
Comment=Эмулятор микрокалькулятора МК-61
Icon=$INSTALL_DIR/icon.png
Exec=/bin/sh $INSTALL_DIR/mk61.sh
Categories=Game;Java;
" > $INSTALL_DIR/mk61.desktop

echo "done"
