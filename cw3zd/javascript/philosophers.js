// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.

    var fork = this;
    var recAcquire = function(waitTime) {
        setTimeout(function() {
            if (fork.state == 0) {
                fork.state = 1;
                cb();
            } else {
                if (waitTime < 1024) { // BEB ma maksymalną wartość wykładnika
                    waitTime *= 2;
                }
                recAcquire(waitTime);
            }
        }, waitTime)
    };
    
    recAcquire(1);
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.EAT_TIME = 10;
    this.THINK_TIME = 10;
    this.eatCount = 0;
    this.waitTime = 0;
    this.waitStartTime = 0;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        philosopher = this;
    
    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    var recNaive = function(count) {
	    if (count > 0) {
            setTimeout(function() {
                philosopher.waitStartTime = new Date().getTime();
                forks[f1].acquire(function() {
                    if (PRINT_ALL) console.log(`Philosopher ${id} has acquired the left fork`);
                    forks[f2].acquire(function() {
                        var deltaTime = new Date().getTime() - philosopher.waitStartTime;
                        philosopher.waitTime += deltaTime;
                        philosopher.eatCount++;
                        if (PRINT_ALL) console.log(`Philosopher ${id} has acquired the right fork and proceeds to eat for the ${philosopher.eatCount}. time`);
                        setTimeout(function() {
                            forks[f1].release();
                            forks[f2].release();
                            if (PRINT_ALL) console.log(`Philosopher ${id} has ended eating`);
                            recNaive(count - 1);
                        }, philosopher.EAT_TIME)
                    })
                })
            }, philosopher.THINK_TIME);
        } else {
            RUNNING_PHILOSOPHERS--;
        }
    };

    // uruchamianie filozofów z losowym opóźnieniem; uruchamiając jednocześnie natychmiast dochodzi do zakleszczenia
    setTimeout(function() {
        recNaive(count)
    }, Math.ceil(Math.random() * 100));
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        f1 = this.id % 2 == 0 ? this.f1 : this.f2, // zamiana widelców w zależności od parzystości filozofa, łatwiejsze niż ifowanie w funkcjach
        f2 = this.id % 2 == 0 ? this.f2 : this.f1,
        id = this.id,
        philosopher = this;

    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    var recAsym = function(count) {
	    if (count > 0) {
            setTimeout(function() {
                philosopher.waitStartTime = new Date().getTime();
                forks[f1].acquire(function() {
                    if (PRINT_ALL) console.log(`Philosopher ${id} has acquired the 1st fork`);
                    forks[f2].acquire(function() {
                        var deltaTime = new Date().getTime() - philosopher.waitStartTime;
                        philosopher.waitTime += deltaTime;
                        philosopher.eatCount++;
                        if (PRINT_ALL) console.log(`Philosopher ${id} has acquired the 2nd fork and proceeds to eat for the ${philosopher.eatCount}. time`);
                        setTimeout(function() {
                            forks[f1].release();
                            forks[f2].release();
                            if (PRINT_ALL) console.log(`Philosopher ${id} has ended eating`);
                            recAsym(count - 1);
                        }, philosopher.EAT_TIME)
                    })
                })
            }, philosopher.THINK_TIME);
        } else {
            RUNNING_PHILOSOPHERS--;
        }
    };

    recAsym(count);
}

Conductor = function(philosophersCount) {
    this.state = philosophersCount - 1;

    return this;
}

Conductor.prototype.acquire = function(cb) {
    var conductor = this;
    
    var recAcquire = function(waitTime) {
        setTimeout(function() {
            if (conductor.state > 0) {
                conductor.state--;
                cb();
            } else {
                if (waitTime < 4096) {
                    waitTime *= 2;
                }
                recAcquire(waitTime);
            }
        }, waitTime)
    };
    
    recAcquire(1);
}

Conductor.prototype.release = function() {
    this.state++;
}

Philosopher.prototype.startConductor = function(count, conductor) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        philosopher = this;
    
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    var recConductor = function(count) {
        if (count > 0) {
            setTimeout(function() {
                philosopher.waitStartTime = new Date().getTime();
                conductor.acquire(function() {
                    forks[f1].acquire(function() {
                        if (PRINT_ALL) console.log(`Philosopher ${id} has acquired the left fork`);
                        forks[f2].acquire(function() {
                            var deltaTime = new Date().getTime() - philosopher.waitStartTime;
                            philosopher.waitTime += deltaTime;
                            philosopher.eatCount++;
                            if (PRINT_ALL) console.log(`Philosopher ${id} has acquired the right fork and proceeds to eat for the ${philosopher.eatCount}. time`);
                            setTimeout(function() {
                                forks[f1].release();
                                forks[f2].release();
                                conductor.release();
                                if (PRINT_ALL) console.log(`Philosopher ${id} has ended eating`);
                                recConductor(count - 1);
                            }, Math.ceil(Math.random() * 10))
                        }) //                 ^
                    }) // losowe czasy jedzenia i myślenia, dla stałych wartości program wykonywał się długo
                }) //              V
            }, Math.ceil(Math.random() * 125));
        } else {
            RUNNING_PHILOSOPHERS--;
        }
    };

    recConductor(count);
}

// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców, 
// a nie każdego z osobna

function acquireBoth(cb, fork1, fork2) {
    var rec = function(waitTime) {
        setTimeout(function() {
            if (fork1.state == 0 && fork2.state == 0) {
                fork1.state = fork2.state = 1;
                cb();
            } else {
                if (waitTime < 1024) {
                    waitTime *= 2;
                }
                rec(waitTime);
            }
        }, waitTime)
    };
    
    rec(1);
}

Philosopher.prototype.startPickupBoth = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        philosopher = this;
    
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    var recPickupBoth = function(count) {
	    if (count > 0) {
            setTimeout(function() {
                philosopher.waitStartTime = new Date().getTime();
                acquireBoth(function() {
                    philosopher.waitTime += new Date().getTime() - philosopher.waitStartTime;
                    philosopher.eatCount++;
                    if (PRINT_ALL) console.log(`Philosopher ${id} has acquired both forks and proceeds to eat for the ${philosopher.eatCount}. time`);
                    setTimeout(function() {
                        forks[f1].release();
                        forks[f2].release();
                        if (PRINT_ALL) console.log(`Philosopher ${id} has ended eating`);
                        recPickupBoth(count - 1);
                    }, philosopher.EAT_TIME)
                }, forks[f1], forks[f2]);
            }, philosopher.THINK_TIME);
        } else {
            RUNNING_PHILOSOPHERS--;
        }
    };

    recPickupBoth(count);
}

var RUNNING_PHILOSOPHERS;
var test = function(N, itersCount, methodName) {
    RUNNING_PHILOSOPHERS = N;
    var forks = [];
    var philosophers = [];
    for (var i = 0; i < N; i++) {
        forks.push(new Fork());
    }
    
    for (var i = 0; i < N; i++) {
        philosophers.push(new Philosopher(i, forks));
    }
    
    var conductor;
    if (methodName == "arbiter") {
        conductor = new Conductor(N);
    }

    for (var i = 0; i < N; i++) {
        if (methodName == "naive") {
            philosophers[i].startNaive(itersCount);
        } else if (methodName == "asym") {
            philosophers[i].startAsym(itersCount);
        } else if (methodName == "arbiter") {
            philosophers[i].startConductor(itersCount, conductor);
        } else if (methodName == "pickupboth") {
            philosophers[i].startPickupBoth(itersCount);
        }
    }

    var waitForPhilosophersTermination = function(cb) {
        setTimeout(function() {
            if (RUNNING_PHILOSOPHERS > 0) {
                waitForPhilosophersTermination(cb);
            } else {
                cb();
            }
        }, 1000)
    }

    waitForPhilosophersTermination(function() {
        process.stdout.write("Average wait times [ms]: ");
        philosophers.forEach(philosopher => process.stdout.write(`${(philosopher.waitTime / itersCount).toFixed(0)}, `));
        console.log();
    });
}

var args = process.argv.slice(2);
if (args.length < 4) {
    console.log("Expected 4 arguments: numberOfPhilosophers, itersCount, methodName <naive/asym/arbiter/pickupboth>, printAll <true/false>");
} else {
    var N = parseInt(args[0]),
    itersCount = parseInt(args[1]),
    methodName = args[2],
    PRINT_ALL = (args[3] == "true");

    test(N, itersCount, methodName);
}
