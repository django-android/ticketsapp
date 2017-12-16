from rest_framework import generics
from django.contrib.auth.models import User
from app import serializers

class RegisterCitizen(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = serializers.UserSerializer
