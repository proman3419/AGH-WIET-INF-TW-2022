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

    // zawsze czekamy na początku acquire, więc można potraktować to jako czas na myślenie

    var fork = this;
    var rec = function(waitTime) {
        setTimeout(function() {
            if (fork.state == 0) {
                fork.state = 1;
                cb();
            } else {
                if (waitTime < 32768) {
                    waitTime *= 2;
                }
                rec(waitTime);
            }
        }, waitTime)
    };
    
    rec(1);
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.EAT_TIME = 1;
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
	    philosopher.waitStartTime = new Date().getTime();
	    if (count > 0) {
            forks[f1].acquire(function() {
                console.log(`Philosopher ${id} has acquired the left fork`);
                forks[f2].acquire(function() {
                    philosopher.totalWaitTime += new Date().getTime() - philosopher.currentWaitStartTime;
                    philosopher.eatCount++;
                    console.log(`Philosopher ${id} has acquired the right fork and proceeds to eat for the ${philosopher.eatCount}. time`);
                    setTimeout(function() {
                        forks[f1].release();
                        forks[f2].release();
                        console.log(`Philosopher ${id} has ended eating`);
                        recNaive(count - 1);
                    }, philosopher.EAT_TIME)
                })
            });
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
	    philosopher.waitStartTime = new Date().getTime();
	    if (count > 0) {
            forks[f1].acquire(function() {
                console.log(`Philosopher ${id} has acquired the 1st fork`);
                forks[f2].acquire(function() {
                    philosopher.totalWaitTime += new Date().getTime() - philosopher.currentWaitStartTime;
                    philosopher.eatCount++;
                    console.log(`Philosopher ${id} has acquired the 2nd fork and proceeds to eat for the ${philosopher.eatCount}. time`);
                    setTimeout(function() {
                        forks[f1].release();
                        forks[f2].release();
                        console.log(`Philosopher ${id} has ended eating`);
                        recAsym(count - 1);
                    }, philosopher.EAT_TIME)
                })
            });
        }
    };

    recAsym(count);
}

// Conductor = function() {
    
// }

// Philosopher.prototype.startConductor = function(count) {
//     var forks = this.forks,
//         f1 = this.f1,
//         f2 = this.f2,
//         id = this.id,
//         philosopher = this;
    
//     // zaimplementuj rozwiązanie z kelnerem
//     // każdy filozof powinien 'count' razy wykonywać cykl
//     // podnoszenia widelców -- jedzenia -- zwalniania widelców

//     var recConductor = function(count) {
// 	    philosopher.waitStartTime = new Date().getTime();
// 	    if (count > 0) {
//             forks[f1].acquire(function() {
//                 console.log(`Philosopher ${id} has acquired the 1st fork`);
//                 forks[f2].acquire(function() {
//                     philosopher.totalWaitTime += new Date().getTime() - philosopher.currentWaitStartTime;
//                     philosopher.eatCount++;
//                     console.log(`Philosopher ${id} has acquired the 2nd fork and proceeds to eat for the ${philosopher.eatCount}. time`);
//                     setTimeout(function() {
//                         forks[f1].release();
//                         forks[f2].release();
//                         console.log(`Philosopher ${id} has ended eating`);
//                         recConductor(count - 1);
//                     }, philosopher.EAT_TIME)
//                 })
//             });
//         }
//     };

//     recConductor(count);
// }

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
                if (waitTime < 32768) {
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
	    philosopher.waitStartTime = new Date().getTime();
	    if (count > 0) {
            acquireBoth(function() {
                philosopher.totalWaitTime += new Date().getTime() - philosopher.currentWaitStartTime;
                philosopher.eatCount++;
                console.log(`Philosopher ${id} has acquired both forks and proceeds to eat for the ${philosopher.eatCount}. time`);
                setTimeout(function() {
                    forks[f1].release();
                    forks[f2].release();
                    console.log(`Philosopher ${id} has ended eating`);
                    recPickupBoth(count - 1);
                }, philosopher.EAT_TIME)
            }, forks[f1], forks[f2]);
        }
    };

    recPickupBoth(count);
}

var N = 5;
var forks = [];
var philosophers = []
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

for (var i = 0; i < N; i++) {
    // philosophers[i].startNaive(10);
    // philosophers[i].startAsym(10);
    philosophers[i].startPickupBoth(10);
}