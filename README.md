# Project 2 - *SimpleTweet*

**SimpleTweet** is an android app that allows a user to view their Twitter timeline. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: 10 hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **sign in to Twitter** using OAuth login
- [x]	User can **view tweets from their home timeline**
- [x] User is displayed the username, name, and body for each tweet
- [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
- [x] User can refresh tweets timeline by pulling down to refresh

The following **optional** features are implemented:

- [x] User can view more tweets as they scroll with infinite pagination
- [x] Improve the user interface and theme the app to feel "twitter branded"
- [x] Links in tweets are clickable and will launch the web browser
- [x] User can tap a tweet to display a "detailed" view of that tweet
- [ ] User can see embedded image media within the tweet detail view
- [ ] User can watch embedded video within the tweet
- [ ] User can open the twitter app offline and see last loaded tweets
- [x] On the Twitter timeline, leverage the CoordinatorLayout to apply scrolling behavior that hides / shows the toolbar.

The following **additional** features are implemented:

- [x] User can view how many likes and retweets a tweet has

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='walkthrough_click_link.gif' title='Video Walkthrough Click Link' width='' alt='Video Walkthrough Click Link' />

<img src='walkthrough_swipe_infinite_scroll.gif' title='Video Walkthrough Infinite Scroll' width='' alt='Video Walkthrough Infinite Scroll' />


GIF created with [Kap](https://getkap.co/).

## Notes

I tried to implement persistence with the database (my attempts are on a different branch), but I did not have the time to work on it. I ran into issues with embedding an object in another object (User in a Tweet) and none of what I tried work, and I ran out of time to debug.

I attempted to implement the collapsing Toolbar, but it doesn't appear to work...? I can't test it now because of rate limits, but if it does work, I'll update the readme + walkthrough gifs.

I spent a lot of time trying to figure out how to add the xml icons into the project. I got it done eventually!

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.