﻿using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class ProductCategory
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public string Name { get; set; } = string.Empty;

        [Required]
        public bool ShowInPage { get; set; } = true;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        public List<Product> Products { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}