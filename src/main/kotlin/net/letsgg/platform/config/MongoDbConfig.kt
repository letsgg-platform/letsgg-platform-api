package net.letsgg.platform.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.mongodb.MongoTransactionManager

import org.springframework.data.mongodb.MongoDatabaseFactory




@EnableMongoAuditing
@EnableMongoRepositories("net.letsgg.platform.repository")
@Configuration
class MongoDbConfig {

//    @Bean
//    fun transactionManager(dbFactory: MongoDatabaseFactory) = MongoTransactionManager(dbFactory)
}