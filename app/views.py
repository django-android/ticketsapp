from django.shortcuts import render
from django.http import HttpResponse
from django.views import View
from django.contrib.auth.models import User
from django.core import serializers
from django.contrib.auth import authenticate, login

import json
# Create your views here.

class Home(View):
    def get(self, request, *arg, **kwargs):
        c = {'foo': 'bar'}
        return HttpResponse(json.dumps(c), content_type='application/json')

class LoginAuth(View):
    def get(self, request, *args, **kwargs):
        username = kwargs['username']
        password = kwargs['password']
        try:
            user = User.objects.get(username=username)
            if user.check_password(password):
                username = user.username
                user = authenticate(username=username, password=password)
                login(request, user)
                details = User.objects.get(username=username)
                serialized_obj = serializers.serialize('json', [details, ])
                return HttpResponse(serialized_obj)
            else:
                return HttpResponse('{"messages":"Wrong password"}')
        except User.DoesNotExist:
            return HttpResponse('{"message": "username and password incorrect"}')
