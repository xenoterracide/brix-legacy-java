/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration;
import org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketMessagingAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketRequesterAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration;
import org.springframework.boot.autoconfigure.rsocket.RSocketStrategiesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.rsocket.RSocketSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import picocli.CommandLine;

@SpringBootApplication(exclude = {
  JacksonAutoConfiguration.class,
  AopAutoConfiguration.class,
  SqlInitializationAutoConfiguration.class,
  TaskExecutionAutoConfiguration.class,
  TaskSchedulingAutoConfiguration.class,
  ActiveMQAutoConfiguration.class,
  ArtemisAutoConfiguration.class,
  BatchAutoConfiguration.class,
  CacheAutoConfiguration.class,
  CassandraAutoConfiguration.class,
  CassandraDataAutoConfiguration.class,
  CassandraReactiveRepositoriesAutoConfiguration.class,
  CassandraReactiveDataAutoConfiguration.class,
  CassandraRepositoriesAutoConfiguration.class,
  ClientHttpConnectorAutoConfiguration.class,
  CodecsAutoConfiguration.class,
  CouchbaseAutoConfiguration.class,
  CouchbaseDataAutoConfiguration.class,
  CouchbaseReactiveDataAutoConfiguration.class,
  CouchbaseReactiveRepositoriesAutoConfiguration.class,
  CouchbaseRepositoriesAutoConfiguration.class,
  DataSourceAutoConfiguration.class,
  DataSourceTransactionManagerAutoConfiguration.class,
  DispatcherServletAutoConfiguration.class,
  ElasticsearchDataAutoConfiguration.class,
  ElasticsearchRepositoriesAutoConfiguration.class,
  ElasticsearchRestClientAutoConfiguration.class,
  EmbeddedLdapAutoConfiguration.class,
  EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
  ErrorMvcAutoConfiguration.class,
  ErrorWebFluxAutoConfiguration.class,
  FlywayAutoConfiguration.class,
  FreeMarkerAutoConfiguration.class,
  EmbeddedMongoAutoConfiguration.class,
  GroovyTemplateAutoConfiguration.class,
  GsonAutoConfiguration.class,
  H2ConsoleAutoConfiguration.class,
  HazelcastAutoConfiguration.class,
  HazelcastJpaDependencyAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class,
  HttpEncodingAutoConfiguration.class,
  HttpHandlerAutoConfiguration.class,
  HypermediaAutoConfiguration.class,
  InfluxDbAutoConfiguration.class,
  IntegrationAutoConfiguration.class,
  JdbcTemplateAutoConfiguration.class,
  // JerseyAutoConfiguration.class,
  JmsAutoConfiguration.class,
  JmxAutoConfiguration.class,
  JndiConnectionFactoryAutoConfiguration.class,
  JndiDataSourceAutoConfiguration.class,
  JooqAutoConfiguration.class,
  JpaRepositoriesAutoConfiguration.class,
  JsonbAutoConfiguration.class,
  JtaAutoConfiguration.class,
  KafkaAutoConfiguration.class,
  LdapAutoConfiguration.class,
  LiquibaseAutoConfiguration.class,
  MailSenderAutoConfiguration.class,
  MessageSourceAutoConfiguration.class,
  MongoAutoConfiguration.class,
  MongoReactiveAutoConfiguration.class,
  MongoReactiveDataAutoConfiguration.class,
  MongoReactiveRepositoriesAutoConfiguration.class,
  MongoReactiveDataAutoConfiguration.class,
  HttpMessageConvertersAutoConfiguration.class,
  JdbcRepositoriesAutoConfiguration.class,
  LdapAutoConfiguration.class,
  LdapRepositoriesAutoConfiguration.class,
  MailSenderAutoConfiguration.class,
  MailSenderValidatorAutoConfiguration.class,
  MongoDataAutoConfiguration.class,
  MongoRepositoriesAutoConfiguration.class,
  MultipartAutoConfiguration.class,
  MustacheAutoConfiguration.class,
  Neo4jAutoConfiguration.class,
  Neo4jDataAutoConfiguration.class,
  Neo4jReactiveDataAutoConfiguration.class,
  Neo4jRepositoriesAutoConfiguration.class,
  Neo4jReactiveRepositoriesAutoConfiguration.class,
  OAuth2ClientAutoConfiguration.class,
  OAuth2ResourceServerAutoConfiguration.class,
  NettyAutoConfiguration.class,
  PersistenceExceptionTranslationAutoConfiguration.class,
  ProjectInfoAutoConfiguration.class,
  QuartzAutoConfiguration.class,
  R2dbcAutoConfiguration.class,
  R2dbcRepositoriesAutoConfiguration.class,
  R2dbcTransactionManagerAutoConfiguration.class,
  RSocketMessagingAutoConfiguration.class,
  R2dbcDataAutoConfiguration.class,
  RSocketRequesterAutoConfiguration.class,
  RSocketSecurityAutoConfiguration.class,
  RSocketServerAutoConfiguration.class,
  RSocketStrategiesAutoConfiguration.class,
  RabbitAutoConfiguration.class,
  ReactiveElasticsearchRepositoriesAutoConfiguration.class,
  ReactiveElasticsearchRestClientAutoConfiguration.class,
  ReactiveOAuth2ClientAutoConfiguration.class,
  ReactiveOAuth2ResourceServerAutoConfiguration.class,
  ReactiveSecurityAutoConfiguration.class,
  ReactiveUserDetailsServiceAutoConfiguration.class,
  ReactiveWebServerFactoryAutoConfiguration.class,
  RedisAutoConfiguration.class,
  RedisReactiveAutoConfiguration.class,
  RedisReactiveAutoConfiguration.class,
  RedisRepositoriesAutoConfiguration.class,
  RepositoryRestMvcAutoConfiguration.class,
  RestTemplateAutoConfiguration.class,
  Saml2RelyingPartyAutoConfiguration.class,
  SecurityAutoConfiguration.class,
  SecurityFilterAutoConfiguration.class,
  SendGridAutoConfiguration.class,
  ServletWebServerFactoryAutoConfiguration.class,
  SessionAutoConfiguration.class,
  SolrAutoConfiguration.class,
  SpringApplicationAdminJmxAutoConfiguration.class,
  SpringDataWebAutoConfiguration.class,
  ThymeleafAutoConfiguration.class,
  TransactionAutoConfiguration.class,
  UserDetailsServiceAutoConfiguration.class,
  ValidationAutoConfiguration.class,
  WebClientAutoConfiguration.class,
  WebFluxAutoConfiguration.class,
  WebMvcAutoConfiguration.class,
  WebServicesAutoConfiguration.class,
  WebSocketMessagingAutoConfiguration.class,
  WebSocketReactiveAutoConfiguration.class,
  WebSocketServletAutoConfiguration.class,
  XADataSourceAutoConfiguration.class,
  WebServiceTemplateAutoConfiguration.class
})
public class Application
  implements CommandLineRunner, ExitCodeGenerator, CommandLine.IExecutionExceptionHandler {

  private final Logger log = LogManager.getLogger( Application.class );

  private final CommandLine.IFactory factory;

  private final CliConfiguration cliCommand;

  private int exitCode;

  Application( CommandLine.IFactory factory, CliConfiguration cliCommand ) {
    this.factory = factory;
    this.cliCommand = cliCommand;
  }

  public static void main( String... args ) {
    System.exit( exec( args ) );
  }

  static int exec( String... args ) {
    return SpringApplication.exit( SpringApplication.run( Application.class, args ) );
  }

  @Override
  public void run( String... args ) {
    var cli = new CommandLine( cliCommand, factory );
    cli.setCaseInsensitiveEnumValuesAllowed( true );
    cli.setExecutionExceptionHandler( this );
    this.exitCode = cli.execute( args );
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }

  @Override
  public int handleExecutionException(
    Exception ex,
    CommandLine commandLine,
    CommandLine.ParseResult parseResult
  ) {
    log.error( ex );
    log.debug( "", ex );
    return 1;
  }
}
