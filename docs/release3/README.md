

## Testing
### Jacoco test coverage
![image](../../assets/release3/jacoco-ui.png)
![image](../../assets/release3/jacoco-core.png)
![image](../../assets/release3/jacoco-rest.png)

More details can be found be opening the report html, created after running tests. These can be found in each module's target/site/jacoco directories

In the schedulelog.json and some other places, there are a few catch block branches that are not covered, but we did not think they have much impact on the app when it runs in a normal user scenario. We did also not test the App.java and RestApplication as these does not have logic created by us. Other than that, we have tried to create test scenarios for as much coverage as possible. A high coverage means that we can ensure that as many use cases as possible stays functional and are not broken by future changes.