package be.henallux.studycard.models;

import be.henallux.studycard.R;

public enum NetworkError {
    NO_CONNECTION(R.drawable.ic_no_connectivity, R.string.http_no_connection),
    REQUEST_ERROR(R.drawable.ic_error, R.string.request_error),
    TECHNICAL_ERROR(R.drawable.ic_error, R.string.technical_error),
    NOT_FOUND(R.drawable.ic_error, R.string.not_found);

    private int errorDrawable;
    private int errorMessage;

    NetworkError(int errorDrawable, int errorMessage) {
        this.errorDrawable = errorDrawable;
        this.errorMessage = errorMessage;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public int getErrorMessage() {
        return errorMessage;
    }
}

