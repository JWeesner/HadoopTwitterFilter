Executing HW3 submission:

0. Run Adam Shook's HW2 example producer and consumer to create sufficient .avro data files to be used
1. Create a /open/tweets and /analytics directories in HDFS with read/write permissions
2. Use Crontab -e and paste in crontab-example.txt replacing jwees1 with your username(all shell scripts require replacing of username as well)
3. Ensure user has execute permissions on all shell scripts
4. Wait for crontab to execute all scripts
5. hdfs dfs -cat /analytics/path_to_file to check output

References:
http://stackoverflow.com/questions/11534041/removing-duplicates-using-piglatin
https://pig.apache.org/docs/r0.9.1/func.html#tokenize
https://github.com/adamjshook/hadoop-demos/blob/master/lol/pig/cronjob/run.sh.template

Spoke with Ted Adams on plans of attack.