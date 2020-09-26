# Sentry & ICM integration module
Integration module for Sentry and ICM 7.10. The module configures, captures additional information (Pipeline dict values) and provide the logback appender for sending logs to Senty.
**Sentry**
[Sentry](https://sentry.io/) is an application monitoring platform that captures error at runtime. With Sentry you can diagnose problems and find bugs in your code.
**Intershop ICM**
[Intershop ICM](https://www.intershop.com/) has a 25-year track record of helping businesses digitalize their sales models and implementing commerce projects.
# Build / Assembly Config
There are two options for producing binaries for this module
- Build this module as a standalone project
- Copy the cartridges to your project

### Build as a standalone project
Create a [gradle-wrapper.properties](https://github.com/ivable/sentry/tree/develop/gradle/wrapper) file with your project's gradle distrution URL in the configuration. 
See [gradle-wrapper.properties.sample](https://github.com/ivable/sentry/blob/develop/gradle/wrapper/gradle-wrapper.properties.sample) file
```
distributionUrl=<URL_TO_GRADLE_DIST>
```
Open a console and run the following command to build and publish to your artifact repository.
```
./gradlew publish -PrunOnCI=true \
                  -PscmVersionExt=RELEASE \
                  -PreleaseURL=https://your-artifact-reposity/repositories/releases \
                  -PrepoUserName=$REPO_UPLOAD_USER -PrepoUserPasswd=$REPO_UPLOAD_USER_PSW
```
In your assembly's `build.gradle` file add the following to the assembly.cartridge closure.
```
// Sentry module
def ivableCartridges = [
    'ac_sentry_io'
]
include (*(ivableCartridges.collect {"com.ivable.common:$it"}), in:[development, test, production])
// Optional test sentry
def ivableTestCartridges = [
    'ac_sentry_io_test'
]
include (*(ivableTestCartridges.collect {"com.ivable.common:$it"}), in:[development, test])

order = listFromAssembly( ...
// add ivable cartridges
order += ivableCartridges + ivableTestCartridges
```
Redeploy the project
`./gradlew deployserver`
### Copy to own project
Copying the source code is straight forward, simple copy the folder `ac_sentry_io` and optionally `ac_sentry_io_test` to your project and build.

In you assembly's `build.gradle` file add the following to the assembly.cartridge closure.
```
// Sentry module
def ivableCartridges = [
    'ac_sentry_io'
]
include (*(ivableCartridges.collect {project(":$it")}), in:[development, test, production])
// Optional test sentry
def ivableTestCartridges = [
    'ac_sentry_io_test'
]
include (*(ivableTestCartridges.collect {project(":$it")), in:[development, test])

order = listFromAssembly( ...
// add ivable cartridges
order += ivableCartridges + ivableTestCartridges
```
Redeploy the project.
`./gradlew deployserver`
# Configure
## DSN

We suggest that you use a configuration cartridge to set the project specific settings. 
An example cartridge can be found here:
https://github.com/ivable/ivable-config


For Sentry to work you'll need a DNS key (see your project setting in Sentry). This DNS key should be configured in a property file so that it is picked up by the [Configuration Framework](https://support.intershop.com/kb/index.php/Display/2R9141).
Set the following key
```
io.sentry.dns=<you-DSN-KEY>
```
## Logback
Add the following logback appender to your project.
An Example can be found [here](https://github.com/ivable/ivable-config/blob/develop/ivable_config/staticfiles/cartridge/logback/logback-ivable_config.xml)
```

<?xml version="1.0" encoding="UTF-8" ?>
<included>
	<!-- BEGIN SENTRY.IO Logback Config-->
	<appender name="Sentry" class="io.sentry.logback.SentryAppender">
		<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
			<!-- Set log level for Sentry -->
			<level>ERROR</level> 
		</filter>
		<!-- Filter out YUI errors to prevent spike protection kicking in-->
		<filter name="YUIJSErrorReporterFilter"
			class="ch.qos.logback.core.filter.EvaluatorFilter">
			<evaluator name="YUIJSErrorFileLogger">
				<expression>!logger.equals("com.intershop.beehive.core.internal.resource.YUIJSErrorReporter")
				</expression>
			</evaluator>
			<OnMatch>NEUTRAL</OnMatch>
			<OnMismatch>DENY</OnMismatch>
		</filter>
	</appender>
	
	<!-- send logs mesg from intershop to Sentry -->
	<logger name="com.intershop">
		<appender-ref ref="Sentry" />
	</logger>
	<!-- send logs mesg from custom project to Sentry -->
	<logger name="COM.MY.PROJECT">
		<appender-ref ref="Sentry" />
	</logger>
	<!-- END SENTRY.IO -->
</included>
```
