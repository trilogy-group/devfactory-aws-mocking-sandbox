workingDir=/app/sandbox-data/
output=$workingDir/rest_export.json
zipFile=$workingDir/smockin-to-import.zip

cd $workingDir

echo "[" > $output
firstMock=1
for f in $workingDir/*aws-service-*.json ; do 
    if [ -f $f ] ; then 
        echo "Adding mocks from: $f"
        if [ $firstMock -eq 0 ]; then
            echo "," >> $output
        else
            firstMock=0
        fi
        cat $f >> $output
    fi
done
echo "]" >> $output

rm -f $zipFile
if [ $firstMock -eq 0 ] ; then
    zip -v $zipFile rest_export.json
else 
    echo "Nothing to import"
fi
