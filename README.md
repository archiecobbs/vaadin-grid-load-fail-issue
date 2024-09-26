# vaadin-grid-load-fail-issue
Demonstration test case for Vaadin bug https://github.com/vaadin/flow/issues/20069

Bug scenario:

1. When the page is first loaded, an empty Grid is displayed
1. One second later, the Grid's data provider signals `refreshAll()` with new data
1. The Grid updates on the server, but the browser display does not immediately update
1. Eventually, when some other unrelated event happens, the browser finally does update

To reproduce the bug on Mac OS:

1. You need to have `tomcat@10` installed via `brew`
1. Run `mvn clean package`
1. Run `./run.sh`
1. Connect to `http://localhost:8080/example/example/`
1. Notice the animated loading indicator spins indefinitely (much longer than 1 second)
    1. You can get it to finish by clicking it, sometimes switching browser tabs, etc.
1. Press the "Search" button repeatedly to trigger new load cycles
1. After the first load, it consistently takes only 1 second to load (which is correct)

To repeat the failure scenario (longer than 1 second load time), just reload the browser tab.
