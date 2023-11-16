provider "aws" {
    region = "ap-south-1"
  }
resource "aws_route53_zone" "primary" {
    name = "example.com"
  }
resource "aws_route53_record" "www-dev" {
  zone_id = aws_route53_zone.primary.zone_id
  name    = "www"
  type    = "CNAME"
  ttl     = 5

  weighted_routing_policy {
    weight = var.service1_www-dev
  }

  set_identifier = "dev"
  records        = ["dev.example.com"]
}

resource "aws_route53_record" "www-live" {
  zone_id = aws_route53_zone.primary.zone_id
  name    = "www"
  type    = "CNAME"
  ttl     = 5

  weighted_routing_policy {
    weight = var.service1_www-live
  }

  set_identifier = "live"
  records        = ["live.example.com"]
}
