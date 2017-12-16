from django.urls import path
from app import views
from app import api_views

urlpatterns = [
    path('home', views.Home.as_view(), name="home"),
    path('api/register', api_views.RegisterCitizen.as_view(), name="register"),
    path('api/api_auth/<str:username>/<str:password>/',views.LoginAuth.as_view(),
        name='api_auth'),
]
