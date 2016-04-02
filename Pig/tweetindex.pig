tweet = LOAD '$input' USING AvroStorage();

tweet_info = FOREACH tweet GENERATE id, text;

all_words = FOREACH tweet_info GENERATE FLATTEN(TOKENIZE(text)), id;

STORE all_words INTO '$output';