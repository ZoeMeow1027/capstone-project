﻿using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    public class UserSession
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("userid")]
        public int UserID { get; set; }

        [Required]
        [JsonProperty("token")]
        public string Token { get; set; } = string.Empty;

        [Required]
        [JsonProperty("datecreated")]
        public DateTime DateCreated { get; set; } = DateTime.UtcNow;

        [Required]
        [JsonProperty("dateexpired")]
        public DateTime DateExpired { get; set; } = DateTime.UtcNow;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [Required]
        [ForeignKey("UserID")]
        [JsonProperty("user")]
        public User User { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
