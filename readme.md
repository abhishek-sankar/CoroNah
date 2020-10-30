![Header Image](https://dev-to-uploads.s3.amazonaws.com/i/vxgk3d03lz3k74lj2d51.png)

### Say what now?

A bit of context first.
> KTU (Kerala Technological University) had plans to conduct offline examinations for the final semesters when COVID-19 was on the rise in Kerala.

Now this poses a great problem. Who is responsible if things take a turn for the worst? The million dollar idea? Maintain a dear diary of all places you've been to. * slow claps *

> Okay, enough context.

The original intention of the app was to serve as a record of all the places you visited, which <b>remains accessible to you</b>. 

However, on July 29th, 2020 the university put out a notice that this is no longer needed since they decide to scrap the idea of conducting exams offline, rather assigned individual colleges to evaluate their final semester undergraduates. 

![Phew](https://dev-to-uploads.s3.amazonaws.com/i/3mos2hhx1jxrhzpc0ymo.gif)

Now while I see that as an absolute win for the students in terms of personal safety, the work I put into building something to assist got blown down the drain. Well, almost. I decided to convert this into an app that keeps a record of your location history. *Don't worry, you wont get an email titled*
> *I know what you did last summer.*

*from me*, and in a perfect world (Yes, the one where PewDiePie is still ahead of T-Series), is able to figure out if you are at risk, and perform contact tracing. It's more of a side project right now, to be honest. 

### What is it gonna be now?

Current plan of action is along these lines:-

Upon giving it permissions, it automatically keeps a record of movement. When things get a bit calmer, and we adapt to the new normal, you need to go to a specific place, an unavoidable meetup (but one where strict social distancing guidelines are followed, duh!) The app generates a one time QR for each individual user as well as each place. 

Two cases here; Lets imagine Im a user, and need to go to the place, lets call it Central Perk. 

![Central Perk](https://dev-to-uploads.s3.amazonaws.com/i/tpxwzyp06nt8ay6p44xs.gif)

I check out QR on Central Perk. If it shows green, it means that no one with the risk of exposure has been in the area for the past 15 days. 

Works the other way too. Im hosting the event, and need to cater to the safety of my guests. Entry is allowed only if the app reports that the person hasn't had exposure to anyone at risk of COVID-19. 

Essentially, provides a sense of security to the organisers and the attendees. 

Quick peek at a few present screens for viewing one's own travel record:

![Screens](https://dev-to-uploads.s3.amazonaws.com/i/ykz2e16lhtcbkelemgeo.png)

The amount of stuff a user has to do is minimal. Most of the work happens via cloud functions on firebase.

#### To wrap up, this is a work in progress, I'll be working on it once my present, more important commitments are over. 

### Contributions
Im sorry, but most of the functionality requires you to have a GCP API Key. If you feel this could use any cool improvements, feel free to raise it as an issue though.

## Credits
[@alananto](https://github.com/aanto07) for help with the ideation.

[@shyleshs](https://github.com/shyleshsunnithan) for the clean designs.

[@mssreerag](https://github.com/mssreerag) for agreeing to help with code in the middle of our internal exams.