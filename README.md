

# an implementation of send effects for polyperc and awake

Here are the changes done to the polyperc engine:

```
1,3c1,3
< // CroneEngine_PolyPerc
< // pulse wave with perc envelopes, triggered on freq
< Engine_PolyPerc : CroneEngine {
---
> // CroneEngine_PolyPerc_fx
> // pulse wave with perc envelopes, triggered on freq, compatibility with fx mod
> Engine_PolyPerc_fx : CroneEngine {
10a11,14
>     var sendA = 0;
>     var sendB = 0;
>     var sendABus = 0;
>     var sendBBus = 0;
18,19c22,23
<     SynthDef("PolyPerc", {
<                       arg out, freq = 440, pw=pw, amp=amp, cutoff=cutoff, gain=gain, release=release, pan=pan;
---
>     SynthDef("PolyPerc_fx", {
>                       arg out, freq = 440, pw=pw, amp=amp, cutoff=cutoff, gain=gain, release=release, pan=pan, sendA=sendA, sendB=sendB, sendABus=sendABus, sendBBus=sendBBus;
23c27,30
<                       Out.ar(out, Pan2.ar((filt*env), pan));
---
>                       var sig = Pan2.ar((filt*env), pan);
>                       Out.ar(out, sig);
>                       Out.ar(sendABus, sendA*sig);
>               Out.ar(sendBBus, sendB*sig);
28c35
<       Synth("PolyPerc", [\out, context.out_b, \freq,val,\pw,pw,\amp,amp,\cutoff,cutoff,\gain,gain,\release,release,\pan,pan], target:pg);
---
>       Synth("PolyPerc_fx", [\out, context.out_b, \freq,val,\pw,pw,\amp,amp,\cutoff,cutoff,\gain,gain,\release,release,\pan,pan,\sendA,sendA,\sendB,sendB,\sendABus,(~sendA ? Server.default.outputBus),\sendBBus, (~sendB ? Server.default.outputBus)], target:pg);
29a37,46
>
>       this.addCommand("send_a", "f", { arg msg;
>         sendA = msg[1];
>       });
>
>       this.addCommand("send_b", "f", { arg msg;
>         sendB = msg[1];
>       });
```
