export authsecret=zogledzki:1142d4c784e3933c8f5447663a640ffce2
outdir=./e2e-results
mkdir -p $outdir
rm -rf $outdir/*
mkdir $outdir/results
global_results=$outdir/results/statuses.txt
echo '"Feature","FAILED","BROKEN","PASSED","SKIPPED","UNKNOWN"' > $global_results
while IFS= read -r ticket
do
    if [ "$ticket" != "" ] ; then
        echo "Processing with ticket: $ticket"

        java -jar jenkins-cli.jar \
          -s http://qa-integration-ci.jenkins.aureacentral.com/ \
          -auth $authsecret \
          build Feature/gherkin-execution-for-feature \
          -s -v \
          -p JIRA_ID=$ticket > $outdir/results/$ticket.output < /dev/null

      jobId=`grep "Completed Feature » gherkin-execution-for-feature" $outdir/results/$ticket.output | cut -d'#' -f 2 | cut -d' ' -f1`

      curl --silent --user $authsecret http://qa-integration-ci.jenkins.aureacentral.com/job/Feature/job/gherkin-execution-for-feature/$jobId/allure/data/behaviors.csv \
      >  $outdir/results/$ticket.status
      grep AWS $outdir/results/$ticket.status | cut -d',' -f2,4- >> $global_results
    fi
done < /tmp/sandbox/e2e-tickets.txt

cat $global_results

status=`cat e2e-results/results/statuses.txt | cut -d',' -f2-3 | grep  '"1"' | wc -l`
if [[ $status -gt 0 ]]; then
    echo "Failure, at least one test failed or got broken! Number of failures/brokes: $status"
    exit 1
else
    echo "Success, None test failed nor got broken!"
    exit 0
fi
