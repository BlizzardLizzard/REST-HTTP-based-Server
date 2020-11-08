public class RequestContext {
     public String request;
     public String HttpType;
     public String URI;

     public RequestContext(String request, String HttpType, String URI)
     {
         this.request = request;
         this.HttpType = HttpType;
         this.URI = URI;
         System.out.println("It worked!");
     }
    public String getRequest() {
        return request;
    }

    public String getHttpType() {
        return HttpType;
    }

    public String getURI() {
        return URI;
    }

}
