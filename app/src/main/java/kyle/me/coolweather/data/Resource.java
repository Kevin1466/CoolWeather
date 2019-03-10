package kyle.me.coolweather.data;

public class Resource<T> {
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int LOADING = 2;

    public int status;
    public T data;
    public String msg;

    public Resource(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public Resource() {
    }

    public Resource<T> success(T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public Resource<T> error(T data, String msg) {
        return new Resource<>(ERROR, data, msg);
    }

    public Resource<T> loading(T data) {
        return new Resource<>(LOADING, data, null);
    }
}
