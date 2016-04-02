tweet = LOAD '$input' USING AvroStorage();

user_groups = GROUP tweet BY user_id;

count_groups = FOREACH user_groups GENERATE group AS id, COUNT(tweet) AS cnt;

user_info = FOREACH tweet GENERATE user_id, screen_name;

count_groups_user = JOIN count_groups BY id, user_info BY user_id;

final = FOREACH count_groups_user GENERATE id, screen_name, cnt;

final_distinct = DISTINCT final;

ordered_final = ORDER final_distinct BY cnt DESC;

limit_final = LIMIT ordered_final 100;

STORE limit_final INTO '$output';