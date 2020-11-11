if [ -f /app/sandbox-data/smockin-to-import.zip ]; then 
    curl --location --request POST 'localhost:8001/mock/import' \
    --header 'KeepExisting: false' \
    --form 'file=@/app/sandbox-data/smockin-to-import.zip'
else 
    curl --location --request POST 'localhost:8001/mock/import' \
    --header 'KeepExisting: false' \
    --form 'file=@/app/smockin-data/smockin_empty-aws_services.zip'

fi