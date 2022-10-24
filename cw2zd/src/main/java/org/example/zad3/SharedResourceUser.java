package org.example.zad3;

public class SharedResourceUser extends Thread {
    private final SharedResource sharedResource;

    public SharedResourceUser(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        sharedResource.performActivity();
    }
}
