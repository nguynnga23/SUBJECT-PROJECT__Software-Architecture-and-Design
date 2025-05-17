variable "aws_region" {
  default = "ap-southeast-1"
}

variable "ami_id" {
  # Ubuntu 22.04 cho ap-southeast-1
  default = "ami-0fa377108253bf620"
}

variable "instance_type" {
  default = "t2.micro"
}

variable "key_name" {
  default = "my-key"
}

variable "public_key_path" {
  default = "/home/huy/.ssh/my-key.pub"
}

variable "private_key_path" {
  default = "/home/huy/.ssh/my-key"
}
