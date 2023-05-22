using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class ImageMetadataService : IImageMetadataService
    {
        private readonly DataContext _context;

        public ImageMetadataService(DataContext context)
        {
            _context = context;
        }

        public void Add(ProductImageMetadata item)
        {
            if (_context.ImageMetadatas.FirstOrDefault(p => p.Name.ToLower() == item.Name.ToLower()) != null)
                throw new ArgumentException(string.Format("Image Metadata with name {0} is exist!", item.Name));

            _context.ImageMetadatas.Add(item);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            };
        }

        public void Delete(ProductImageMetadata item)
        {
            var data = _context.ImageMetadatas.FirstOrDefault(p => p.ID == item.ID);
            if (data == null)
                throw new ArgumentException(string.Format("Image Metadata with ID {0} is exist!", item.ID));

            _context.ImageMetadatas.Remove(data);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            };
        }

        public ProductImageMetadata? Get(int id)
        {
            return _context.ImageMetadatas.FirstOrDefault(p => p.ID == id);
        }

        public ProductImageMetadata? Get(string name)
        {
            return _context.ImageMetadatas.FirstOrDefault(p => p.Name.ToLower() == name.ToLower());
        }

        public List<ProductImageMetadata> GetAll()
        {
            return _context.ImageMetadatas.ToList();
        }

        public void Update(ProductImageMetadata item)
        {
            var data = _context.ImageMetadatas.FirstOrDefault(p => p.ID == item.ID);
            if (data == null)
                throw new ArgumentException(string.Format("Image Metadata with ID {0} is exist!", item.ID));

            data.Name = item.Name;
            data.Description = item.Description;
            data.DateModified = DateTimeOffset.Now.ToUnixTimeMilliseconds();

            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            };
        }
    }
}
