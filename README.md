# vaadin-grid-load-fail-issue
Demonstration test case for Vaadin Flow bugs #20240 and #6349

### [Bug #20240](https://github.com/vaadin/flow/issues/20240)

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

### [Bug #6349](https://github.com/vaadin/flow/issues/6349)

Bug scenario:

1. `UI.getCurrent()` returns the wrong value

To reproduce the bug on Mac OS:

1. You need to have `tomcat@10` installed via `brew`
1. Run `mvn clean package`
1. Run `./run.sh`
1. Open browser tab #1 to `http://localhost:8080/example/example/other`
    1. It should say "This is the first toot"
1. Open browser tab #2 to `http://localhost:8080/example/example/other`
    1. It should say "This is not the first toot"
1. Press the button on broswer tab #1
1. Press the button on broswer tab #2
1. Browser tab #2 will show "My current UI" to be the "actual UI" for tab #1
