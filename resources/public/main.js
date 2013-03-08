goog.addDependency("base.js", ['goog'], []);
goog.addDependency("../cljs/core.js", ['cljs.core'], ['goog.array', 'goog.object', 'goog.string.format', 'goog.string.StringBuffer', 'goog.string']);
goog.addDependency("../boids/hello.js", ['boids.hello'], ['cljs.core']);
goog.addDependency("../clojure/browser/event.js", ['clojure.browser.event'], ['cljs.core', 'goog.events.EventType', 'goog.events.EventTarget', 'goog.events']);
goog.addDependency("../clojure/browser/net.js", ['clojure.browser.net'], ['goog.net.xpc.CrossPageChannel', 'clojure.browser.event', 'goog.net.xpc.CfgFields', 'cljs.core', 'goog.net.EventType', 'goog.json', 'goog.net.XhrIo']);
goog.addDependency("../clojure/browser/repl.js", ['clojure.browser.repl'], ['clojure.browser.event', 'clojure.browser.net', 'cljs.core']);
goog.addDependency("../boids/brepl.js", ['boids.brepl'], ['cljs.core', 'clojure.browser.repl']);