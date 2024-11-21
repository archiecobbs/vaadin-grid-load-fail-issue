# vaadin-grid-load-fail-issue
Demonstration test case for Vaadin Flow bug #6849

### [Bug #6849](https://github.com/vaadin/flow-components/issues/6849)

Bug scenario:

1. When the Grid is displayed, the Grid column headers are not aligned with the columns themselves.
1. If you grab one of the separators between the column headers and slide it, the headers then "snap" into their correct places.

To reproduce the bug on Mac OS:

1. You need to have `tomcat@10` installed via `brew`
1. Run `mvn clean package`
1. Run `./run.sh`
1. Connect to `http://localhost:8080/example/example/`

