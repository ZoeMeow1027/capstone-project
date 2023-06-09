﻿using Newtonsoft.Json;
using PhoneStoreManager.Model.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("BillSummary")]
    public class BillSummary
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("userid")]
        public int? UserID { get; set; } = null;

        [Required]
        [JsonProperty("recipient")]
        public string Recipient { get; set; } = string.Empty;

        [Required]
        [JsonProperty("recipientaddress")]
        public string RecipientAddress { get; set; } = string.Empty;

        [Required]
        [JsonProperty("recipientcountrycode")]
        public string RecipientCountryCode { get; set; } = string.Empty;

        [Required]
        [JsonProperty("recipientphone")]
        public string RecipientPhone { get; set; } = string.Empty;

        [Required]
        [JsonProperty("datecreated")]
        public long DateCreated { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [JsonProperty("datecompleted")]
        public long? DateCompleted{ get; set; } = null;

        [JsonProperty("shippingprice")]
        public double ShippingPrice { get; set; } = 0;

        [JsonProperty("vat")]
        public double VAT { get; set; } = 0;

        [JsonProperty("discount")]
        public double Discount { get; set; } = 0;

        [Required]
        [JsonProperty("totalprice")]
        public double TotalPrice { get; set; } = 0;

        [Required]
        [JsonProperty("status")]
        public DeliverStatus Status { get; set; } = DeliverStatus.WaitingForConfirm;

        [JsonProperty("statusaddress")]
        public string? StatusAddress { get; set; }

        [JsonProperty("statusadditional")]
        public string? StatusAdditional { get; set; }

        [Required]
        [JsonProperty("paymentmethod")]
        public PaymentMethod PaymentMethod { get; set; } = PaymentMethod.Unselected;

        [JsonProperty("paymentmethodname")]
        public string? PaymentMethodName { get; set; } = null;

        [JsonProperty("paymentid")]
        public string? PaymentID { get; set; } = null;

        [Required]
        [JsonProperty("paymentcompleted")]
        public bool PaymentCompleted { get; set; } = false;

        [JsonProperty("usermessage")]
        public string? UserMessage { get; set; } = null;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [ForeignKey("UserID")]
        [JsonProperty("user")]
        public User? User { get; set; } = null;

        [JsonProperty("billdetails")]
        public List<BillDetails> BillDetails { get; set; }

        [JsonIgnore]
        public List<Warranty> Warranties { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
