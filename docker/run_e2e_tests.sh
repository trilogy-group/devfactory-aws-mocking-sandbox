outdir=/app/sandbox-tests/e2e-results
mkdir -p $outdir
rm -rf $outdir/*
mkdir $outdir/generated-features
mkdir $outdir/tickets
mkdir $outdir/results
while IFS= read -r ticket
do
    if [ "$ticket" != "" ] ; then
        echo "Processing with ticket: $ticket"

        e2e-fetch --ticket $ticket --output-folder $outdir/generated-features

        if [ -f $outdir/generated-features/${ticket}.feature ]; then
            # Change dynamically env to run on
            dest_env=dev_zibi
            sed "s|\(Given       environment\) \"[^\"]*\" \(from.*$\)|\1 \"$dest_env\" \2|g" \
                $outdir/generated-features/$ticket.feature > $outdir/tickets/$ticket.feature

            # run test
            e2e-behave $outdir/tickets/$ticket.feature \
               --summary \
               --outfile $outdir/results/$ticket.output.txt \
               --logging-location $outdir/results
        else
            echo "Can't fetch feature $ticket'"
            exit 1
        fi
    fi
done < /app/sandbox-tests/e2e-tickets.txt

