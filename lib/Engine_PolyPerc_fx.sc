// CroneEngine_PolyPerc_fx
// pulse wave with perc envelopes, triggered on freq, compatibility with fx mod
Engine_PolyPerc_fx : CroneEngine {
	var pg;
    var amp=0.3;
    var release=0.5;
    var pw=0.5;
    var cutoff=1000;
    var gain=2;
    var pan = 0;
    var sendA = 0;
    var sendB = 0;
    var sendABus = 0;
    var sendBBus = 0;

	*new { arg context, doneCallback;
		^super.new(context, doneCallback);
	}

	alloc {
		pg = ParGroup.tail(context.xg);
    SynthDef("PolyPerc_fx", {
			arg out, freq = 440, pw=pw, amp=amp, cutoff=cutoff, gain=gain, release=release, pan=pan, sendA=sendA, sendB=sendB, sendABus=sendABus, sendBBus=sendBBus;
			var snd = Pulse.ar(freq, pw);
			var filt = MoogFF.ar(snd,cutoff,gain);
			var env = Env.perc(level: amp, releaseTime: release).kr(2);
			var sig = Pan2.ar((filt*env), pan);
			Out.ar(out, sig);
			Out.ar(sendABus, sendA*sig);
        	Out.ar(sendBBus, sendB*sig);
		}).add;

		this.addCommand("hz", "f", { arg msg;
			var val = msg[1];
      Synth("PolyPerc_fx", [\out, context.out_b, \freq,val,\pw,pw,\amp,amp,\cutoff,cutoff,\gain,gain,\release,release,\pan,pan,\sendA,sendA,\sendB,sendB,\sendABus,(~sendA ? Server.default.outputBus),\sendBBus, (~sendB ? Server.default.outputBus)], target:pg);
		});

		this.addCommand("send_a", "f", { arg msg;
      sendA = msg[1];
    });

    this.addCommand("send_b", "f", { arg msg;
      sendB = msg[1];
    });

		this.addCommand("amp", "f", { arg msg;
			amp = msg[1];
		});

		this.addCommand("pw", "f", { arg msg;
			pw = msg[1];
		});
		
		this.addCommand("release", "f", { arg msg;
			release = msg[1];
		});
		
		this.addCommand("cutoff", "f", { arg msg;
			cutoff = msg[1];
		});
		
		this.addCommand("gain", "f", { arg msg;
			gain = msg[1];
		});
		
		this.addCommand("pan", "f", { arg msg;
		  postln("pan: " ++ msg[1]);
			pan = msg[1];
		});
	}

	free { 
		pg.free;
	}
}
