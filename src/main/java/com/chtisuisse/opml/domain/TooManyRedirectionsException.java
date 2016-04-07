package com.chtisuisse.opml.domain;

/**
 * We do not allow the feed to redirect to new urls forever
 * Created by Christophe on 07.04.2016.
 */
class TooManyRedirectionsException extends Exception {

    private final String feedURL;

    public TooManyRedirectionsException(String feedURL) {
        this.feedURL = feedURL;
    }

    @Override
    public String getMessage() {
        return "This url " + feedURL + " has reached the maximum of redirects";
    }
}
