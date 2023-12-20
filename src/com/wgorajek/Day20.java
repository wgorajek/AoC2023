package com.wgorajek;

import java.util.*;
import java.util.function.Predicate;

public class Day20 extends Solution {
    @Override
    public Object getPart1Solution() {
        var machine = getInput();
        int totalLow = 0;
        int totalHigh = 0;
        Queue<Signal> signals = new LinkedList<Signal>();
        for (var i = 0; i < 1000; i++) {
            signals.add(new Signal(machine.modules.get("broadcaster"), Pulse.LOW, machine.modules.get("broadcaster")));
            totalLow++;
            while (!signals.isEmpty()) {
                var signal = signals.remove();
                if (signal.reciever != null) {
                    var response = signal.reciever.recieveSignal(signal.pulse, signal.sender.name);
                    if (response != Pulse.NONE) {
                        for (var destination : signal.reciever.destinations) {
                            signals.add(new Signal(machine.modules.get(destination), response, signal.reciever));
                            if (response == Pulse.LOW) {
                                totalLow++;
                            } else {
                                totalHigh++;
                            }
                        }
                    }
                }
            }

        }

        return totalHigh * totalLow;
    }


    @Override
    public Object getPart2Solution() {
        var machine = getInput();
        Queue<Signal> signals = new LinkedList<Signal>();
        var vm = 0;
        var lastVm = 0;
        var lm = 0;
        var lastlm = 0;
        var jd = 0;
        var lastjd = 0;
        var fv = 0;
        var lastfv = 0;
        for (var i = 0; i < 100000; i++) {
            signals.add(new Signal(machine.modules.get("broadcaster"), Pulse.LOW, machine.modules.get("broadcaster")));
            while (!signals.isEmpty()) {
                var signal = signals.remove();
                if (signal.reciever != null) {
                    if (signal.reciever.name.equals("vm") && signal.pulse == Pulse.LOW) {
                        if (lastVm != 0) {
                            vm = i - lastVm;
                        }
                        lastVm = i;
                    }
                    if (signal.reciever.name.equals("lm") && signal.pulse == Pulse.LOW) {
                        if (lastVm != 0) {
                            lm = i - lastlm;
                        }
                        lastlm = i;
                    }
                    if (signal.reciever.name.equals("jd") && signal.pulse == Pulse.LOW) {
                        if (lastjd != 0) {
                            jd = i - lastjd;
                        }
                        lastjd = i;
                    }
                    if (signal.reciever.name.equals("fv") && signal.pulse == Pulse.LOW) {
                        if (lastfv != 0) {
                            fv = i - lastfv;
                        }
                        lastfv = i;
                    }
                    var response = signal.reciever.recieveSignal(signal.pulse, signal.sender.name);
                    if (response != Pulse.NONE) {
                        for (var destination : signal.reciever.destinations) {
                            signals.add(new Signal(machine.modules.get(destination), response, signal.reciever));
                        }
                    }
                }

            }


        }
        return (long) vm * lm * jd * fv;
    }

    public enum ModuleType {
        BROADCAST,
        FLIPFLOP,
        CONJUNCTION
    }

    public enum Pulse {
        NONE,
        LOW,
        HIGH
    }

    public class Signal {
        Module reciever;
        Pulse pulse;
        Module sender;

        public Signal(Module reciever, Pulse pulse, Module sender) {
            this.reciever = reciever;
            this.pulse = pulse;
            this.sender = sender;
        }
    }

    public class Module {
        String name;
        ModuleType type;
        Boolean isOn = false;
        HashMap<String, Pulse> lastPulses = new HashMap<String, Pulse>();
        ArrayList<String> destinations = new ArrayList<String>();

        public boolean allLastPulsesHigh() {
            for (var pulse : lastPulses.values()) {
                if (pulse == Pulse.LOW) {
                    return false;
                }
            }
            return true;
        }

        public Pulse recieveSignal(Pulse pulse, String sender) {
            if (type == ModuleType.BROADCAST) {
                return pulse;
            } else if (type == ModuleType.CONJUNCTION) {
                lastPulses.put(sender, pulse);
                if (allLastPulsesHigh()) {
                    return Pulse.LOW;
                } else {
                    return Pulse.HIGH;
                }


            } else if (type == ModuleType.FLIPFLOP) {
                if (pulse == Pulse.LOW) {
                    if (isOn) {
                        isOn = false;
                        return Pulse.LOW;
                    } else {
                        isOn = true;
                        return Pulse.HIGH;
                    }
                }
            }
            return Pulse.NONE;
        }
    }


    public class Machine {
        HashMap<String, Module> modules = new HashMap<String, Module>();

    }

    private Machine getInput() {
        var input = getInputLines();
        Machine machine = new Machine();
        for (var line : input) {
            var split = line.split("->");
            var name = split[0].trim();

            var module = new Module();
            if (name.equals("broadcaster")) {
                module.type = ModuleType.BROADCAST;
                module.name = "broadcaster";
            } else {
                module.name = name.substring(1);
                if (name.contains("%")) {
                    module.type = ModuleType.FLIPFLOP;
                } else {
                    module.type = ModuleType.CONJUNCTION;
                }
            }
            machine.modules.put(module.name, module);

            var destinations = split[1].split(",");
            for (var destination : destinations) {
                module.destinations.add(destination.trim());
            }
        }
        for (var module : machine.modules.values()) {
            if (module.type == ModuleType.CONJUNCTION) {
                for (var moduleSender : machine.modules.values()) {
                    if (moduleSender.destinations.contains(module.name)) {
                        module.lastPulses.put(moduleSender.name, Pulse.LOW);
                    }
                }
            }
        }

        return machine;
    }

}
