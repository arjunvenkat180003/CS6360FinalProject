df <- read.csv("C:/Users/ashur/OneDrive/Documents/Programs/database/twitter_dataset.csv")
#generating random coordinates
df$latitude <- round(runif(10000, 40, 50), digits = 2)
df$longitude <- round(runif(10000, -40, -30), digits = 2)
#select random word from tweet
select_random_word <- function(text) {
  words <- unlist(strsplit(text, "[. ]+"))
  random_index <- sample(length(words), 1)
  return(tolower(trimws(words[random_index])))
}

df$keyword <- sapply(df$Text, select_random_word)
write.csv(newdf, file = "C:/Users/ashur/OneDrive/Documents/Programs/database/edited_twitter_dataset.csv")
