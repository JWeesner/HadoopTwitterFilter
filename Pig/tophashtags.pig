tweet = LOAD '$input' USING AvroStorage();

tags = FOREACH tweet GENERATE FLATTEN(hashtags) AS tag, id AS id;

grouped_tags = GROUP tags BY tag;

count_tags = FOREACH grouped_tags GENERATE group AS tag, COUNT(tags) AS cnt;

ordered_tags = ORDER count_tags BY cnt DESC;

limited_tags = LIMIT ordered_tags 100;

join_tags = JOIN tags BY tag, limited_tags BY tag;

final_tags = FOREACH join_tags GENERATE id, limited_tags::tag, cnt;

final_order = ORDER final_tags BY cnt DESC;

STORE final_order INTO '$output';