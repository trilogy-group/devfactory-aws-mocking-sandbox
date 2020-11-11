if [ -f /app/sandbox-data/update.sh ]; then
  /app/sandbox-data/update.sh
fi

sh -c 'sleep 60; /app/create_import_archive.sh ; /app/import_smockin.sh' &

cd /app/smockin
./run.sh -CONSOLE
