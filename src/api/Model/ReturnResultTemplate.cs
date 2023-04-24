using System.Dynamic;

namespace PhoneStoreManager.Model
{
    public class ReturnResultTemplate
    {
        public ReturnResultTemplate(int statusCode = 200, string message = "", dynamic? data = null)
        {
            StatusCode = statusCode;
            Message = message;
            Data = data;
        }

        public int StatusCode { get; set; } = 200;

        public string Message { get; set; } = string.Empty;

        public dynamic? Data { get; set; } = null;

        public dynamic ToDynamicObject()
        {
            dynamic obj = new ExpandoObject();
            obj.code = StatusCode;
            obj.msg = Message;
            obj.data = Data;
            return obj;
        }
    }
}
