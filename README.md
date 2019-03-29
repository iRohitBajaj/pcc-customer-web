# Demo app for PCC Session-state Caching

This project is a snapshot that captures the issue associated with
session-state caching in PCC when domain objects do not implement
`java.io.Serializable`.

## Setup

The application can be deployed as-is given the following assumptions.

-   A MySql service has been created and named `bookshop-db` as referenced
    in the `manifest.yml` file.

-   A PCC service has been created and configured for session-state
    caching and bound to the deployed application after deploying.
    Later, you can add the service binding to the manifest.

## Duplicating the issue

Once the application has been deployed, you can hit the application
endpoint to obtain the main page.
The page has been designed to collect a customer ID from 1001-2000
and display the associated customer info.
You can return to the home page and the same customer will be displayed
because the customer id is stored as a session attribute.
With session-state caching enabled, this should allow the session
information to be cached in PCC, surviving application restart.

Restart the app with `cf restart customer-web`.
You will see that on re-start and refreshing the home page, you will
be required to re-enter the customer ID.
This should NOT be the case if session-state caching were actually
working.
On watching the log file (`cf logs customer-web`) while performing the
above, you will see output similar to the following.

```bash
   2019-03-29T13:12:41.34-0600 [RTR/1] OUT
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT java.io.NotSerializableException: io.pivotal.bookshop.domain.Customer
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1184) ~[na:1.8.0_192]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348) ~[na:1.8.0_192]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.internal.InternalDataSerializer.writeSerializableObject(InternalDataSerializer.java:2351) ~[geode-core-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.internal.InternalDataSerializer.basicWriteObject(InternalDataSerializer.java:2214) ~[geode-core-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.DataSerializer.writeObject(DataSerializer.java:2871) ~[geode-core-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.internal.util.BlobHelper.serializeToBlob(BlobHelper.java:53) ~[geode-core-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.internal.util.BlobHelper.serializeToBlob(BlobHelper.java:43) ~[geode-core-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.modules.session.catalina.DeltaSession8.serialize(DeltaSession8.java:555) [geode-modules-tomcat8-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.geode.modules.session.catalina.DeltaSession8.setAttribute(DeltaSession8.java:234) [geode-modules-tomcat8-9.0.3.jar:na]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.catalina.session.StandardSession.setAttribute(StandardSession.java:1415) [catalina.jar:8.5.34]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.apache.catalina.session.StandardSessionFacade.setAttribute(StandardSessionFacade.java:137) [catalina.jar:8.5.34]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.springframework.web.context.request.ServletRequestAttributes.setAttribute(ServletRequestAttributes.java:183) [spring-web-5.0.5.RELEASE.jar:5.0.5.RELEASE]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.springframework.web.bind.support.DefaultSessionAttributeStore.storeAttribute(DefaultSessionAttributeStore.java:56) [spring-web-5.0.5.RELEASE.jar:5.0.5.RELEASE]
   2019-03-29T13:12:41.33-0600 [APP/PROC/WEB/0] OUT 	at org.springframework.web.method.annotation.SessionAttributesHandler.lambda$storeAttributes$0(SessionAttributesHandler.java:117) [spring-web-5.0.5.RELEASE.jar:5.0.5.RELEASE]
...
```
