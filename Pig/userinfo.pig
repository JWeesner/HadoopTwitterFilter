part = LOAD '$input' USING AvroStorage();

user_info = FOREACH part GENERATE
    user_id,
    screen_name,
    location,
    description,
    followers_count,
    statuses_count,
    geo_enabled,
    lang;

user_groups = GROUP user_info BY user_id;

filtered = FOREACH user_groups{
	unique = LIMIT user_info 1;
	GENERATE FLATTEN(unique);
}

STORE filtered INTO '$output';