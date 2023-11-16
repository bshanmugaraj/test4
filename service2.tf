resource "aws_route53_record" "www-dev2" {
  zone_id = aws_route53_zone.primary.zone_id
  name    = "www"
  type    = "CNAME"
  ttl     = 5

  weighted_routing_policy {
    weight = var.service2_www-dev2
  }

  set_identifier = "prod"
  records        = ["prod.example.com"]
}

resource "aws_route53_record" "www-live2" {
  zone_id = aws_route53_zone.primary.zone_id
  name    = "www"
  type    = "CNAME"
  ttl     = 5

  weighted_routing_policy {
    weight = var.service2_www-live2
  }

  set_identifier = "nonprod"
  records        = ["nonprod.example.com"]
}
