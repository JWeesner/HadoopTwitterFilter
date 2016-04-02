tweet = LOAD '$input' USING AvroStorage();

user_info = FOREACH tweet GENERATE user_id, screen_name, followers_count, statuses_count;

user_groups = GROUP user_info BY user_id;

filtered = FOREACH user_groups{
	unique = LIMIT user_info 1;
	GENERATE FLATTEN(unique);
}

ordered_users = ORDER filtered BY followers_count DESC;

limit_users = LIMIT ordered_users 100;

STORE limit_users INTO '$output';